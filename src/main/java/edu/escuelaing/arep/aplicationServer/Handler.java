package edu.escuelaing.arep.aplicationServer;

@FunctionalInterface
public interface Handler {
     Object process() throws ReflectiveOperationException;
}
