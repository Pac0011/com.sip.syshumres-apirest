package com.sip.syshumres_apirest.resources;

import java.beans.PropertyEditorSupport;

import com.sip.syshumres_utils.StringTrim;

public class StringCleanerEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String cleaned = StringTrim.trimAndRemoveDiacriticalMarks(text);
        setValue(cleaned);
    }

}
