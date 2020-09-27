package com.robert.reyes.payments.utils.mediator;

public abstract class CommandHandler<C extends Command<Response>, Response> {
    abstract public Response handle(C command) throws Exception;
}
