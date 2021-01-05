package blxt.qjava.autovalue.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/12/20 23:26
 */
public class ResolverUtil<T> {
    /*
     * An instance of Log to use for logging in this class.
     */
    private static final Logger log = LoggerFactory.getLogger(ResolverUtil.class);

    /**
     * A simple interface that specifies how to test classes to determine if they
     * are to be included in the results produced by the ResolverUtil.
     */
    public static interface Test {
        /**
         * Will be called repeatedly with candidate classes. Must return True if a class
         * is to be included in the results, false otherwise.
         */
        boolean matches(Class<?> type);
    }

    /**
     * A Test that checks to see if each class is assignable to the provided class. Note
     * that this test will match the parent type itself if it is presented for matching.
     */
    public static class IsA implements Test {
        private Class<?> parent;

        /** Constructs an IsA test using the supplied Class as the parent class/interface. */
        public IsA(Class<?> parentType) {
            this.parent = parentType;
        }

        /** Returns true if type is assignable to the parent type supplied in the constructor. */
        @Override
        public boolean matches(Class<?> type) {
            return type != null && parent.isAssignableFrom(type);
        }

        @Override
        public String toString() {
            return "is assignable to " + parent.getSimpleName();
        }
    }

    /**
     * A Test that checks to see if each class is annotated with a specific annotation. If it
     * is, then the test returns true, otherwise false.
     */
    public static class AnnotatedWith implements Test {
        private Class<? extends Annotation> annotation;

        /** Constructs an AnnotatedWith test for the specified annotation type. */
        public AnnotatedWith(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        /** Returns true if the type is annotated with the class provided to the constructor. */
        @Override
        public boolean matches(Class<?> type) {
            return type != null && type.isAnnotationPresent(annotation);
        }

        @Override
        public String toString() {
            return "annotated with @" + annotation.getSimpleName();
        }
    }

    /** The set of matches being accumulated. */
    private Set<Class<? extends T>> matches = new HashSet<Class<? extends T>>();

    /**
     * The ClassLoader to use when looking for classes. If null then the ClassLoader returned
     * by Thread.currentThread().getContextClassLoader() will be used.
     */
    private ClassLoader classloader;

    /**
     * Provides access to the classes discovered so far. If no calls have been made to
     * any of the {@code find()} methods, this set will be empty.
     *
     * @return the set of classes that have been discovered.
     */
    public Set<Class<? extends T>> getClasses() {
        return matches;
    }

    public ClassLoader getClassLoader() {
        return classloader == null ? Thread.currentThread().getContextClassLoader() : classloader;
    }

    public void setClassLoader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public ResolverUtil<T> findImplementations(Class<?> parent, String... packageNames) {
        if (packageNames == null) {
            return this;
        }

        Test test = new IsA(parent);
        for (String pkg : packageNames) {
            find(test, pkg);
        }

        return this;
    }


    public ResolverUtil<T> findAnnotated(Class<? extends Annotation> annotation, String... packageNames) {
        if (packageNames == null) {
            return this;
        }

        Test test = new AnnotatedWith(annotation);
        for (String pkg : packageNames) {
            find(test, pkg);
        }

        return this;
    }

    public ResolverUtil<T> find(Test test, String packageName) {
        String path = getPackagePath(packageName);

//        try {
//            List<String> children = VFS.getInstance().list(path);
//            for (String child : children) {
//                if (child.endsWith(".class")) {
//                    addIfMatching(test, child);
//                }
//            }
//        } catch (IOException ioe) {
//            log.error("Could not read package: " + packageName, ioe);
//        }

        return this;
    }


    public ResolverUtil<T> find(String packageName, Test test) {
        return find(test, packageName);
    }

    public ResolverUtil<T> find(String packageName, Test ...test) {
        String path = getPackagePath(packageName);

//        try {
//            List<String> children = VFS.getInstance().list(path);
//            for (String child : children) {
//                if (child.endsWith(".class")) {
//                    addIfMatching(child, test);
//                }
//            }
//        } catch (IOException ioe) {
//            log.error("Could not read package: " + packageName, ioe);
//        }

        return this;
    }


    protected String getPackagePath(String packageName) {
        return packageName == null ? null : packageName.replace('.', '/');
    }

    @SuppressWarnings("unchecked")
    protected void addIfMatching(Test test, String fqn) {
        try {
            String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
            ClassLoader loader = getClassLoader();
            log.debug("Checking to see if class " + externalName + " matches criteria [" + test + "]");

            Class<?> type = loader.loadClass(externalName);
            if (test.matches(type)) {
                matches.add((Class<T>) type);
            }
        } catch (Throwable t) {
            log.warn("Could not examine class '" + fqn + "'" + " due to a " +
                    t.getClass().getName() + " with message: " + t.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    protected void addIfMatching( String fqn, Test ...test) {
        try {
            String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
            ClassLoader loader = getClassLoader();
            log.debug("Checking to see if class " + externalName + " matches criteria [" + test + "]");

            Class<?> type = loader.loadClass(externalName);
            boolean flag = false;
            for (int i = 0; i < test.length; i++) {
                if (test[i].matches(type)) {
                    flag = true;
                    break;
                }
            }
            if(flag){
                matches.add((Class<T>) type);
            }
        } catch (Throwable t) {
            log.warn("Could not examine class '" + fqn + "'" + " due to a " +
                    t.getClass().getName() + " with message: " + t.getMessage());
        }
    }
}