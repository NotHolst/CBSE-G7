package dk.gruppe7.common.utils;

import java.util.Random;

public class RandomUtil 
{
    private static Random random = new Random();
    
    public static int GetRandomInteger(int range) {
        return random.nextInt(range);
    }
    
    public static int GetRandomInteger(int min, int max) {
        if(min > max) {
            throw new IllegalArgumentException("Min cannot be lower than max!");
        }
        
        return random.nextInt(max + 1 - min) + min;
    }
}