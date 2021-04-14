/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.Movement;

import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author Nathan
 */
public class DummyJCheckBoxMenuItem extends JCheckBoxMenuItem {

    private String id;

    // id so we know what layer it is for
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    // either construct normally
    public DummyJCheckBoxMenuItem() {
        super();
    }

    // or construct clone
    public DummyJCheckBoxMenuItem(JCheckBoxMenuItem another, String id) {  
        this.id = id;

        this.setEnabled(another.isEnabled());
        this.setSelected(another.isSelected());

        this.setText(another.getText());
        this.setAccelerator(another.getAccelerator());
        this.setIcon(another.getIcon());

        for (ItemListener i : another.getItemListeners()) {
            this.addItemListener(i);
        }
    }
}
