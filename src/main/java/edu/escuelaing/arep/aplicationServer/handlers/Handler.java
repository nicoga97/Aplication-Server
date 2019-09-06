package edu.escuelaing.arep.aplicationServer.handlers;

@FunctionalInterface
public interface Handler {
     Object process() throws ReflectiveOperationException;
}
