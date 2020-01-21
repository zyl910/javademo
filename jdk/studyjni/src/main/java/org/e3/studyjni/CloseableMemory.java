package org.e3.studyjni;

import com.sun.jna.Memory;

import java.io.Closeable;
import java.io.IOException;

/** 具有自动关闭功能的 Memory.
 *
 */
public class CloseableMemory extends Memory implements AutoCloseable {
    /** Create CloseableMemory without parameters.
     *
     */
    protected CloseableMemory() {
        super();
    }

    /** Create CloseableMemory with size parameters. Allocate space in the native heap via a call to C's malloc.
     *
     * @param size number of bytes of space to allocate
     */
    public CloseableMemory(long size) {
        super(size);
    }

    @Override
    protected synchronized void dispose() {
        if (!valid()) return;
        super.dispose();
        size = 0;   // fix size.
    }

    @Override
    public void close() {
        if (!valid()) return;
        dispose();
    }
}
