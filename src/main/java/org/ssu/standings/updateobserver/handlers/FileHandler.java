package org.ssu.standings.updateobserver.handlers;

import org.ssu.standings.updateobserver.Response;

import java.io.IOException;

public interface FileHandler {
    Response getUpdates() throws IOException;
}
