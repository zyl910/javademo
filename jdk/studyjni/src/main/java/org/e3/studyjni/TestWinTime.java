package org.e3.studyjni;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

/** Test Windows time api .
 *
 * <p>Kernel32.dll:</p>
 * <ul>
 *     <li>GetLocalTime function: https://docs.microsoft.com/zh-cn/windows/win32/api/sysinfoapi/nf-sysinfoapi-getlocaltime
 *     <br/><c>void GetLocalTime(LPSYSTEMTIME lpSystemTime);</c>
 *     </li>
 * </ul>
 */
public class TestWinTime {
    /** Windows library - Kernel32.dll
     */
    public interface Kernel32Library extends StdCallLibrary
    {
        @Structure.FieldOrder({ "wYear", "wMonth", "wDayOfWeek", "wDay", "wHour", "wMinute", "wSecond", "wMilliseconds" })
        public static class SYSTEMTIME extends Structure {
            public short wYear;
            public short wMonth;
            public short wDayOfWeek;
            public short wDay;
            public short wHour;
            public short wMinute;
            public short wSecond;
            public short wMilliseconds;
        }

        void GetLocalTime (SYSTEMTIME result);
    }

    public static void main(String[] args) {
        Kernel32Library lib = (Kernel32Library) Native.load("kernel32", Kernel32Library.class);
        Kernel32Library.SYSTEMTIME time = new Kernel32Library.SYSTEMTIME();
        lib.GetLocalTime (time);
        System.out.println("== TestWinTime ==");
        System.out.println ("Year is "+time.wYear);
        System.out.println ("Month is "+time.wMonth);
        System.out.println ("Day of Week is "+time.wDayOfWeek);
        System.out.println ("Day is "+time.wDay);
        System.out.println ("Hour is "+time.wHour);
        System.out.println ("Minute is "+time.wMinute);
        System.out.println ("Second is "+time.wSecond);
        System.out.println ("Milliseconds are "+time.wMilliseconds);
        System.out.println();
        // System.getProperties
        System.out.println("System.getProperties:");
        System.getProperties().list(System.out);
    }
}
