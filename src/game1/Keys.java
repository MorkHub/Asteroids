package game1;

import utilities.Action;
import utilities.Controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game1.Constants.*;

public class Keys extends KeyAdapter implements Controller {
    Action action;
    public Keys() {
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
                action.turn = -1;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                action.turn = +1;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_S:
                action.shoot = true;
                break;
            case KeyEvent.VK_F1:
                action.info = !action.info;
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                action.thrust = 0;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                action.turn = 0;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_S:
                action.shoot = false;
                break;
        }
    }
}
