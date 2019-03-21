package game1;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import utilities.Action;
import utilities.Controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keys extends KeyAdapter implements Controller {
    private Action action;

    Keys() {
        action = new Action();
    }

    @Override
    public Action action() {
        // this is defined to comply with the standard interface
        return action;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_PAGE_UP:
                Constants.game.level++;
                break;
            case KeyEvent.VK_PAGE_DOWN:
                Constants.game.level--;
                break;
            case KeyEvent.VK_END:
                Constants.game.reset = true;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                action.thrust = 1;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                action.turnLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                action.turnRight = true;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_S:
                action.shoot = true;
                break;
            case KeyEvent.VK_F2:
                if (Constants.game != null && Constants.game.ship != null)
                    Constants.game.ship.invulnerableFor(1000);
                break;
            case KeyEvent.VK_F1:
                action.info = !action.info;
                break;
            case KeyEvent.VK_H:
                action.debug = true;
                break;
            case KeyEvent.VK_1:
                action.selectedWeapon = 1;
                Constants.game.addObject(Title.showTitle(Constants.game.view, "Selected weapon 1: regular"));
                break;
            case KeyEvent.VK_2:
                action.selectedWeapon = 2;
                Constants.game.addObject(Title.showTitle(Constants.game.view, "Selected weapon 2: alphabullets"));
                break;
            case KeyEvent.VK_ESCAPE:
                if(Constants.game != null)
                    Constants.game.over();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                action.thrust = 0;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                action.turnLeft = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                action.turnRight = false;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_S:
                action.shoot = false;
                break;
            case KeyEvent.VK_H:
                action.debug = false;
                break;
        }
    }
}
