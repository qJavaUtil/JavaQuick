package test.demo;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;

@AutoService(Processor.class)
public class TestProcessor extends AbstractProcessor// implements AnnotationProcessorFactory
{
    public TestProcessor(){
        System.out.printf("初始化注解处理器:TestProcessor");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 规定需要处理的注解
        System.out.printf("获取到注解@Test");
        return Collections.singleton(Test.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.printf("执行注解@Test解析");
        return false;
    }

}
