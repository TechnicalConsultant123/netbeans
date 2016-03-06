package org.black.kotlin.resolve.lang.java.structure;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.load.java.structure.JavaAnnotation;
import org.jetbrains.kotlin.load.java.structure.JavaClassifierType;
import org.jetbrains.kotlin.load.java.structure.JavaField;
import org.jetbrains.kotlin.load.java.structure.JavaMethod;
import org.jetbrains.kotlin.load.java.structure.JavaType;
import org.jetbrains.kotlin.load.java.structure.JavaTypeParameter;

/**
 *
 * @author Александр
 */
public class NetBeansJavaElementFactory {

    private NetBeansJavaElementFactory(){}
    
    private interface Factory<Binding, Java> {
        @NotNull
        Java create(@NotNull Binding binding);
    }
    
    private static class Factories {
        private static final Factory<Element, JavaAnnotation> ANNOTATIONS =
                new Factory<Element, JavaAnnotation>() {
            @Override
            public JavaAnnotation create(Element binding) {
                return new NetBeansJavaAnnotation(binding);
            }
        };
        
        private static final Factory<TypeMirror, JavaType> TYPES = 
                new Factory<TypeMirror, JavaType>() {
            @Override
            public JavaType create(TypeMirror binding) {
                return NetBeansJavaType.create(binding);
            }
        };
        
        private static final Factory<TypeMirror, JavaClassifierType> CLASSIFIER_TYPES =
                new Factory<TypeMirror, JavaClassifierType>() {
            @Override
            public JavaClassifierType create(TypeMirror binding) {
                return new NetBeansJavaClassifierType(binding);
            }
        };
        
        private static final Factory<Element, JavaMethod> METHODS =
                new Factory<Element, JavaMethod>() {
            @Override
            public JavaMethod create(Element binding) {
                return new NetBeansJavaMethod(binding);
            }
        };
        
        private static final Factory<Element, JavaField> FIELDS =
                new Factory<Element, JavaField>() {
            @Override
            public JavaField create(Element binding) {
                return new NetBeansJavaField(binding);
            }
        };
        
        private static final Factory<TypeParameterElement, JavaTypeParameter> TYPE_PARAMETERS =
                new Factory<TypeParameterElement, JavaTypeParameter>() {
            @Override
            public JavaTypeParameter create(TypeParameterElement binding) {
                return new NetBeansJavaTypeParameter(binding);
            }
        };
        
    }

    @NotNull
    private static <Binding, Java> List<Java> convert(@NotNull Binding[] elements, 
            @NotNull Factory<Binding, Java> factory){
        if (elements.length == 0)
            return Collections.emptyList();
        List<Java> result = Lists.newArrayList();
        for (Binding element : elements){
            result.add(factory.create(element));
        }
        return result;
    }
    
    
    @NotNull
    public static List<JavaAnnotation> annotations(@NotNull Element[] annotations){
        return convert(annotations, Factories.ANNOTATIONS);
    }
    
    @NotNull
    public static List<JavaType> types(@NotNull TypeMirror[] types) {
        return convert(types, Factories.TYPES);
    }
    
    @NotNull
    public static List<JavaClassifierType> classifierTypes(@NotNull TypeMirror[] classTypes){
        return convert(classTypes, Factories.CLASSIFIER_TYPES);
    }
    
    @NotNull
    public static List<JavaMethod> methods(@NotNull Element[] methods){
        return convert(methods, Factories.METHODS);
    }
    
    @NotNull
    public static List<JavaField> fields(@NotNull Element[] variables){
        return convert(variables, Factories.FIELDS);
    }
    
    @NotNull
    public static List<JavaTypeParameter> typeParameters(@NotNull TypeParameterElement[] typeParameters){
        return convert(typeParameters, Factories.TYPE_PARAMETERS);
    }
    
}
