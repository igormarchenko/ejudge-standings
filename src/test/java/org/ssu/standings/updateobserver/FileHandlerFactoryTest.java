package org.ssu.standings.updateobserver;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.ssu.standings.updateobserver.handlers.FileHandler;
import org.ssu.standings.updateobserver.handlers.HTTPFileHandler;
import org.ssu.standings.updateobserver.handlers.LocalFileHandler;

public class FileHandlerFactoryTest {
    @Test
    public void getInstanceToLocalFile() throws Exception {
        FileHandler handler = FileHandlerFactory.getInstance().createFileHandler("/home/usr/standings.xml");
        Assert.assertThat(handler, CoreMatchers.instanceOf(LocalFileHandler.class));
    }

    @Test
    public void getInstanceToHTTPFile() throws Exception {
        FileHandler handler = FileHandlerFactory.getInstance().createFileHandler("http://google.com/standings.xml");
        Assert.assertThat(handler, CoreMatchers.instanceOf(HTTPFileHandler.class));
    }


}