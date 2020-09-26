package com.robert.reyes.payments.utils.mediator;

public interface MediatorPipeline {
    <C extends Command<R>, R> R send(C command) throws Exception;
}
