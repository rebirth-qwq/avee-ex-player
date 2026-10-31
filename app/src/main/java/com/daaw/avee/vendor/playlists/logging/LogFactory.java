package com.daaw.avee.vendor.playlists.logging;

import com.daaw.avee.vendor.compat.commons.logging.Log;

/* JADX INFO: loaded from: classes2.dex */
public class LogFactory {
    public static Log getLog(Class<?> cls) {
        return new MyLog();
    }

    static class MyLog implements Log {
        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void debug(Object obj) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void debug(Object obj, Throwable th) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void error(Object obj) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void error(Object obj, Throwable th) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void fatal(Object obj) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void fatal(Object obj, Throwable th) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void info(Object obj) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void info(Object obj, Throwable th) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public boolean isDebugEnabled() {
            return false;
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public boolean isErrorEnabled() {
            return false;
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public boolean isFatalEnabled() {
            return false;
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public boolean isInfoEnabled() {
            return false;
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public boolean isTraceEnabled() {
            return false;
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public boolean isWarnEnabled() {
            return false;
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void trace(Object obj) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void trace(Object obj, Throwable th) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void warn(Object obj) {
        }

        @Override // com.daaw.avee.vendor.compat.commons.logging.Log
        public void warn(Object obj, Throwable th) {
        }

        MyLog() {
        }
    }
}
