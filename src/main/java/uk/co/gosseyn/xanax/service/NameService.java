package uk.co.gosseyn.xanax.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class NameService {
    //TODO threadsafe?
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    final Resource nameGeneratorJs;
    public NameService(
            @Value("classpath:js/name-generator.js") Resource nameGeneratorJs) {
        this.nameGeneratorJs = nameGeneratorJs;
    }

    public String newName() {
        try {
            InputStreamReader reader = new InputStreamReader(nameGeneratorJs.getInputStream());
            engine.eval(reader);
            Invocable invocable = (Invocable) engine;

            return invocable.invokeFunction("generate", (int)(Math.random() * 10) + 2).toString()
                    +" "+invocable.invokeFunction("generate", (int)(Math.random() * 10) + 2).toString();
        } catch (ScriptException | NoSuchMethodException | IOException e) {
            throw new RuntimeException("Unable to generate name.", e);
        }

    }
}
