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
        double peso = 20.0;
        assertTrue(personalBestRecord.validWeight(peso));

        //Peso válido
        peso = 100.0;
        assertTrue(personalBestRecord.validWeight(peso));

        //Peso válido
        peso = 1.0;
        assertTrue(personalBestRecord.validWeight(peso));

        //Peso válido
        peso = 1.5;
        assertTrue(personalBestRecord.validWeight(peso));

        //Peso no válido
        peso = 1000.0;
        assertFalse(personalBestRecord.validWeight(peso));

        //Peso no válido
        peso = -1.0;
        assertFalse(personalBestRecord.validWeight(peso));

        //Peso no válido
        peso = 10.888;
        assertFalse(personalBestRecord.validWeight(peso));
    }
}

