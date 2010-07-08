package tupianxiangqi;
import java.awt.*;
import javax.swing.*;
import java.net.URL;
import java.awt.event.*;

/**
 * <p>Title: °Ù¼Ò˜·</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: yst</p>
 * @author not attributable
 * @version 1.0
 */

public class Picbutton extends JButton
{
    Image img,imgDown,imgWork,imgMove;

    public Picbutton()
    {
        super();
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void doMouseDown()
    {
        if(imgDown==null) return;
        if(imgWork==null)
            imgWork=this.createImage(this.getWidth(),this.getHeight());

        Graphics g=imgWork.getGraphics();
        g.drawImage(imgDown,0,0,this);
        this.repaint();
    }
    public void doMouseUp()
    {
        if(img==null) return;
        if(imgWork==null)
            imgWork=this.createImage(this.getWidth(),this.getHeight());
        Graphics g=imgWork.getGraphics();
        g.drawImage(img,0,0,this);
        this.repaint();
    }

    public void doMouseEnter()
    {
        if(imgMove==null) return;
        if(imgWork==null)
            imgWork=this.createImage(this.getWidth(),this.getHeight());
        Graphics g=imgWork.getGraphics();
        g.drawImage(imgMove,0,0,this);
        this.repaint();
    }

    public Picbutton(String strNor,String strDown,String strMove)
    {
        super();
        try
        {
         jbInit();
        }
        catch(Exception e2)
        {
        }
        if(strNor!=null&&strNor.length()>0)
        {
            try
            {
                //URL url=pai.Frame1.class.getResource(str);
                img=new ImageIcon(strNor).getImage();

            }
            catch(Exception e2)
            {
            }
        }
        if(strDown!=null&&strDown.length()>0)
        {
            try
            {
                //URL url=pai.Frame1.class.getResource(str);
                imgDown=new ImageIcon(strDown).getImage();
            }
            catch(Exception e3)
            {
            }
        }
        if(strMove!=null&&strMove.length()>0)
        {
            try
            {
                //URL url=pai.Frame1.class.getResource(str);
                imgMove=new ImageIcon(strMove).getImage();

            }
            catch(Exception e2)
            {
            }
        }
    }

    public void paint(Graphics g)
    {
        if(imgWork!=null)
            g.drawImage(imgWork,0,0,this);
        else if(img!=null)
            g.drawImage(img,0,0,this);
        else
            super.paint(g);

    }

    public void update(Graphics g)
    {
             paint(g);
    }
    private void jbInit() throws Exception
    {
        this.addMouseListener(new Picbutton_this_mouseAdapter(this));
    }

    void this_mouseEntered(MouseEvent e)
    {
        doMouseEnter();
    }

    void this_mouseExited(MouseEvent e)
    {
        doMouseUp();
    }

    void this_mousePressed(MouseEvent e)
    {
        doMouseDown();
    }

    void this_mouseReleased(MouseEvent e)
    {
        doMouseUp();
    }
}

class Picbutton_this_mouseAdapter extends java.awt.event.MouseAdapter
{
    Picbutton adaptee;

    Picbutton_this_mouseAdapter(Picbutton adaptee)
    {
        this.adaptee = adaptee;
    }
    public void mouseEntered(MouseEvent e)
    {
        adaptee.this_mouseEntered(e);
    }
    public void mouseExited(MouseEvent e)
    {
        adaptee.this_mouseExited(e);
    }
    public void mousePressed(MouseEvent e)
    {
        adaptee.this_mousePressed(e);
    }
    public void mouseReleased(MouseEvent e)
    {
        adaptee.this_mouseReleased(e);
    }
}