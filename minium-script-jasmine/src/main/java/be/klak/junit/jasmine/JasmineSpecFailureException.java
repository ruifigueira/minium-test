package be.klak.junit.jasmine;

import org.mozilla.javascript.NativeObject;

class JasmineSpecFailureException extends Exception {

    private static final long serialVersionUID = -1801793582585954123L;

    private final String message;
    // private ScriptableObject trace;

    public JasmineSpecFailureException(NativeObject specResultItem) {
        // TODO extract a stracktrace from rhino trace
        // this.trace = (ScriptableObject) specResultItem.get("trace", specResultItem);
        this.message = specResultItem.get("message", specResultItem).toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

}
