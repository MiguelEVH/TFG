package com.example.tfg.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PersonalBestRecordTest {

    private PersonalBestRecord personalBestRecord;

    @Before
    public void setUp(){
        //Nueva marca personal
        personalBestRecord = new PersonalBestRecord();
    }

    @Test
    public void validWeightTest() {

        //Peso válido
        Double peso1 = 20.0;
        assertTrue(personalBestRecord.validWeight(peso1));

        //Peso válido
        Double peso2 = 100.0;
        assertTrue(personalBestRecord.validWeight(peso2));

        //Peso válido
        Double peso3 = 1.0;
        assertTrue(personalBestRecord.validWeight(peso3));

        //Peso no válido
        Double peso4 = 1000.0;
        assertFalse(personalBestRecord.validWeight(peso4));

        //Peso no válido
        Double peso5 = -1.0;
        assertFalse(personalBestRecord.validWeight(peso5));
    }
}