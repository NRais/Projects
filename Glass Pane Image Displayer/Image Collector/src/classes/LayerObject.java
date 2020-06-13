/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nuser
 */
public class LayerObject {
        
    public Boolean selected;
    public String name;
    
    public List<ImageWindowObject> layer;
    
    
    public LayerObject(String layerName) {
        layer = new ArrayList<>();
        selected = false;
        
        this.name = layerName;
    }
    
}
