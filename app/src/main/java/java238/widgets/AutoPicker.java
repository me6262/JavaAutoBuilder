package java238.widgets;

import ch.bailu.gtk.adw.*;
import ch.bailu.gtk.gtk.*;
import java238.App;
import java238.background.Amode;

import java.util.List;

public class AutoPicker {
    public Flap flap;
    public StackSidebar sidebar;
    public AmodeStack stack;

    public AutoPicker() {

        flap = new Flap();
        Box flapBox = new Box(Orientation.VERTICAL, 0);
        Box vbox = new Box(Orientation.VERTICAL, 0);
        var bin = new Bin();
        Frame amodeFrame = new Frame("Long test to see if folding works properly lets keep going so i can test");

        sidebar = new StackSidebar();
        stack = new AmodeStack();
        sidebar.setStack(stack);
        flap.setFoldPolicy(FlapFoldPolicy.AUTO);
        flap.setVexpand(true);


        flap.setFoldThresholdPolicy(FoldThresholdPolicy.NATURAL);
        flap.setFlapPosition(PositionType.LEFT);

        flap.setLocked(true);
        flap.setModal(true);

//        amodeFrame.setChild(flapBox);
        bin.setChild(vbox);
        flap.setFlap(sidebar);
        flap.setContent(stack);

        flap.setHexpand(true);

        Separator sep = new Separator(Orientation.VERTICAL);
//        flap.setSeparator(sep);
        flap.setSwipeToClose(true);
        flap.setSwipeToOpen(true);

    }

    public void setModes(List<Amode> list) {
        for (Amode modeList : list) {
            setMode((Amode) modeList);
        }
        App.window.present();
        flap.show();
    }

    public void setMode(Amode list) {
        AmodeEditorWidget widget = new AmodeEditorWidget(list);
        stack.addTitled(widget, widget.getModeName());
    }



    public void onOpenClicked() {

        flap.setRevealFlap(!flap.getRevealFlap());
        App.window.present();

    }
}