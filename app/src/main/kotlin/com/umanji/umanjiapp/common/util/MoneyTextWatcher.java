package com.umanji.umanjiapp.common.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by paulhwang on 28/02/2018.
 * *************************************** JAVA Code ***********************************************
 */

public class MoneyTextWatcher implements TextWatcher {
    private String TAG = "MoneyTextWatcher";

    private final WeakReference<EditText> editTextWeakReference;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String s = editable.toString();
        if (s.isEmpty()) return;
        editText.removeTextChangedListener(this);
        String cleanString = s.replaceAll("[₩,.]", "");

        BigDecimal parsed = new BigDecimal
                (cleanString).setScale(0, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(1), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getInstance().format(parsed);

        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

}
