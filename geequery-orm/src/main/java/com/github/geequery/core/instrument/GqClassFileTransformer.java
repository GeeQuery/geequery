package com.github.geequery.core.instrument;

import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.ProtectionDomain;

import com.github.geequery.codegen.EnhanceTaskASM;
import com.github.geequery.tools.IOUtils;
import com.github.geequery.tools.resource.ClasspathLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GqClassFileTransformer implements java.lang.instrument.ClassFileTransformer {
    private Logger log = LoggerFactory.getLogger(GqClassFileTransformer.class);

    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        if (name.startsWith("com/github/geequery") || name.startsWith("org/apache") || name.startsWith("javax/"))
            return null;
        if (name.indexOf('$') > -1) {
            return null;
        }
        URL u1 = loader.getResource(name.replace('.', '/') + ".class");
        if (u1 == null)
            return null;

        EnhanceTaskASM task = new EnhanceTaskASM(new ClasspathLoader(false, loader));
        byte[] enhanced;
        try {
            enhanced = task.doEnhance(IOUtils.toByteArray(u1), null);
        } catch (Exception e) {
            log.error("类动态增强错误", e);
            return null;
        }
        if (enhanced == null || enhanced.length == 0) {
            return null;
        }
        log.info("Runtime Enhance Class For Easyframe ORM: {}", name);
        return enhanced;
    }

}
