package com.example.anar.gridlayout;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryValidationTest {
    private MainActivity testActivity = new MainActivity();

    @Test
    public void validInput_True() {
        assertThat(testActivity.inputIsValid("ad340"), is(true));
    }

    @Test
    public void emptyInput_False() {
        assertThat(testActivity.inputIsValid(""), is(false));
    }
}
