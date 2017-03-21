/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dispose;

import collision.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies H
 */
public class DisposeData
{
    private static List<DisposeEvent> events = new ArrayList<>();

    public static List<DisposeEvent> getEvents()
    {
        return events;
    }
}
