package java238.widgets;

import ch.bailu.gtk.adw.*;
import ch.bailu.gtk.gdk.Rectangle;
import ch.bailu.gtk.gtk.*;
import java238.App;

import javax.swing.text.Position;

public class AutoPicker {
    public Flap flap;
    public ListBox modes;

    public AutoPicker() {

        AmodeCommandsWidget amodeCommandsWidget = new AmodeCommandsWidget();
        flap = new Flap();

        Box flapBox = new Box(Orientation.VERTICAL, 0);
        Box vbox = new Box(Orientation.VERTICAL, 0);
        var bin = new Bin();
        Frame amodeFrame = new Frame("Long test to see if folding works properly lets keep going so i can test");

        flap.setFoldPolicy(FlapFoldPolicy.AUTO);
        flap.setVexpand(true);


        flap.setFoldThresholdPolicy(FoldThresholdPolicy.NATURAL);
        flap.setFlapPosition(PositionType.LEFT);

        modes = new ListBox();
        var label = new Label("hababjhgfhjgfahdhasgdhasgdhjagdhgashdgshjdgajhdgahjsdgahjsgdjhgfhgfjghfjhgf");
        Label label2 = new Label("haha i am iasdlkjfhasdjkfhalkdjfhalksjdfhalksjdhfalksjdhflakjdhfn a thingy");
        Label label3 = new Label("hahaafhaskjldfhalksdjfhalksdjhfalkdsjhfakdsjfhaskdjhfalskdjhfa i am in a thingy");
        Label label4 = new Label("2325454645342132435asdfadfadfasfasdfasdfasdfasdfasdfadfasdfadfasdfasdfasdf465");
        flap.setLocked(true);
        flap.setModal(true);
        PreferencesPage amodes = new PreferencesPage();
        var rows1 = new PreferencesGroup();

        amodes.add(rows1);


        PreferencesGroup amodeGroup = new PreferencesGroup();
        amodeGroup.setMarginBottom(10);
        amodeGroup.setMarginEnd(10);
        amodeGroup.setMarginStart(10);
        amodeGroup.setMarginTop(10);
        ActionRow preferencesRow = new ActionRow();
        preferencesRow.setTitle("Tejzhjkhdfajhsdfjst");
        preferencesRow.setTitleLines(1);
        preferencesRow.setHeader(new Label("jhajkhakjlf"));
        ActionRow preferencesRow2 = new ActionRow();
        preferencesRow2.setTitle("Tejzhjkhdfajhsdfjst");
        preferencesRow2.setTitleLines(1);
        preferencesRow2.setHeader(new Label("jhajkhakjlf"));
        ActionRow preferencesRow3 = new ActionRow();
        preferencesRow3.setTitle("Tejzhjkhdfajhsdfjst");
        preferencesRow3.setTitleLines(1);
        preferencesRow3.setHeader(new Label("jhajkhakjlf"));
        amodeGroup.setTitle("test");
        amodeGroup.setDescription("a lot of words here so i can try stuff");
        amodeGroup.add(preferencesRow);
        amodeGroup.add(preferencesRow2);
        amodeGroup.add(preferencesRow3);


        System.out.println(flap.getFlapPosition());
        flapBox.append(label);
        flapBox.append(label2);
        flapBox.append(label3);
        flapBox.append(label4);
        flapBox.setVexpand(true);
//        flapBox.setHexpand(true);
        vbox.append(amodeGroup);
        amodeGroup.setHexpand(false);
//        amodeFrame.setChild(flapBox);
        bin.setChild(vbox);
        flap.setFlap(bin);
        flap.setContent(amodeCommandsWidget.getWidget());

        flap.setHexpand(true);

        Separator sep = new Separator(Orientation.VERTICAL);
//        flap.setSeparator(sep);
        flap.setSwipeToClose(true);
        flap.setSwipeToOpen(true);

    }

    public void onOpenClicked() {

        flap.setRevealFlap(!flap.getRevealFlap());
        App.window.present();

    }
}