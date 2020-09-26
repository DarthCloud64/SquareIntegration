package com.robert.reyes.payments.utils.mediator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.annotation.RequestScope;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Component
@RequestScope
public class MediatorPipelineImpl implements MediatorPipeline {
    @Autowired
    private List<CommandHandler> commandHandlers;

    @Override
    public <C extends Command<R>, R> R send(C command) throws Exception {
        Optional<CommandHandler> requiredCommandHandler = (Optional<CommandHandler>)commandHandlers.stream().filter(commandHandler -> ((ParameterizedType)commandHandler.getClass().getGenericSuperclass()).getActualTypeArguments()[0] == command.getClass()).findFirst();

        if(!requiredCommandHandler.isPresent()){
            throw new Exception("Could not find command handler for type " + command.getClass().toString());
        }
        return (R)requiredCommandHandler.get().handle(command);
    }
}
