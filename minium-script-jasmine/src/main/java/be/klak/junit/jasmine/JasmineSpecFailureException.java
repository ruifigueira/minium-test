package be.klak.junit.jasmine;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.UniqueTag;

class JasmineSpecFailureException extends Exception {

    private static final long serialVersionUID = -1801793582585954123L;

    private final String message;
    // private ScriptableObject trace;

    public JasmineSpecFailureException(NativeObject specResultItem) {
    	ScriptableObject trace = (ScriptableObject) specResultItem.get("trace", specResultItem);
    	Object stack = trace.get("stack", trace);
    	if(stack.equals(UniqueTag.NOT_FOUND)) {
    		this.message = specResultItem.get("message", specResultItem).toString();
    	} else {
    		this.message = specResultItem.get("message", specResultItem).toString() + stack;
    	}
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
