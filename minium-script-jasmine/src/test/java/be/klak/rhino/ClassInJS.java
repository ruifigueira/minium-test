package be.klak.rhino;

import org.mozilla.javascript.ScriptableObject;

public class ClassInJS extends ScriptableObject {

    private static final long serialVersionUID = -9066670485203794921L;

    private int prop;

    public ClassInJS() {
    }

    public void increaseProp() {
        prop++;
    }

    public String jsFunction_fn() {
        return "fn";
    }

    public int jsGet_prop() {
        return prop;
    }

    @Override
    public String getClassName() {
        return "ClassInJS";
    }
}
