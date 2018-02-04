package org.tsdes.intro.spring.testing.coverage.instrumentation;

import org.objectweb.asm.ClassReader;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This classloader will add probes to the loaded classes.
 */
public class InstrumentingClassLoader extends ClassLoader {

    private final Instrumentator instrumentator;
    private final ClassLoader classLoader;
    private final Map<String, Class<?>> classes;

    /**
     * @param packagePrefixesToCover: a "," separated list of package prefixes or class names.
     *                              For example, "com.foo.,com.bar.Bar".
     * @throws IllegalArgumentException if {@code packagePrefixesToCover} is invalid
     */
    public InstrumentingClassLoader(String packagePrefixesToCover)throws IllegalArgumentException {
        instrumentator = new Instrumentator(packagePrefixesToCover);
        classLoader = InstrumentingClassLoader.class.getClassLoader();
        classes = new LinkedHashMap<>();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        if (!instrumentator.canInstrumentForCoverage(ClassName.get(name))) {
            return loadNonInstrumented(name);
        }

        Class<?> result = classes.get(name);
        if (result != null) {
            return result;
        } else {
            ClassName className = new ClassName(name);
            Class<?> instrumentedClass = instrumentClass(className);

            if(instrumentedClass == null){
                return loadNonInstrumented(name);
            }

            return instrumentedClass;
        }
    }

    private Class<?> loadNonInstrumented(String name) throws ClassNotFoundException {
        Class<?> result = findLoadedClass(name);
        if (result != null) {
            return result;
        }
        result = classLoader.loadClass(name);
        return result;
    }

    private Class<?> instrumentClass(ClassName className) throws ClassNotFoundException {

        try (InputStream is = classLoader.getResourceAsStream(className.getAsResourcePath())) {

            if (is == null) {
                System.out.println("Failed to find resource file for "+className.getAsResourcePath());
                return null;
            }

            byte[] byteBuffer = instrumentator.transformBytes(this, className, new ClassReader(is));
            createPackageDefinition(className.getFullNameWithDots());

            Class<?> result = defineClass(className.getFullNameWithDots(), byteBuffer, 0, byteBuffer.length);
            classes.put(className.getFullNameWithDots(), result);


            return result;
        } catch (Throwable t) {
            throw new ClassNotFoundException("Error while loading class" +
                    className.getFullNameWithDots() + " : " + t.getMessage(), t);
        }
    }

    /**
     * Before a new class is defined, we need to create a package definition for it
     *
     * @param className
     */
    private void createPackageDefinition(String className) {
        int i = className.lastIndexOf('.');
        if (i != -1) {
            String pkgname = className.substring(0, i);
            // Check if package already loaded.
            Package pkg = getPackage(pkgname);
            if (pkg == null) {
                definePackage(pkgname, null, null, null, null, null, null, null);
            }
        }
    }
}
