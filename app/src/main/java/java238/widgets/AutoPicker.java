package java238.widgets;

import ch.bailu.gtk.adw.*;
import ch.bailu.gtk.gdk.Rectangle;
import ch.bailu.gtk.gtk.*;
import java238.App;
import java238.background.AmodeCommandList;
import java238.background.AmodeList;
import org.checkerframework.checker.units.qual.A;

import javax.swing.text.Position;
import java.util.List;

public class AutoPicker {
    public Flap flap;
    public ListBox modes;
    PreferencesGroup amodeGroup;
    AmodeCommandsWidget amodeCommandsWidget;

    public AutoPicker() {

        amodeCommandsWidget = new AmodeCommandsWidget();
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


        amodeGroup = new PreferencesGroup();
        amodeGroup.setMarginBottom(10);
        amodeGroup.setMarginEnd(10);
        amodeGroup.setMarginStart(10);
        amodeGroup.setMarginTop(10);
        amodeGroup.setTitle("test");
        amodeGroup.setDescription("a lot of words here so i can try stuff");


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

    public void setModes(List<AmodeList> list) {
        for (AmodeList modeList : list) {
            setMode(modeList);
        }
        App.window.present();
        flap.show();
    }

    public void setMode(AmodeList list) {
        ActionRow row = new ActionRow();
        Button editButton = new Button();
        editButton.setIconName("document-edit-symbolic");
        row.setTitle(list.getName());
        row.setActivatable(true);
        row.onActivate(() -> {
            amodeCommandsWidget.setAuto(list);

        });
        row.addSuffix(editButton);
        amodeGroup.add(row);

    }

    public void onOpenClicked() {

        flap.setRevealFlap(!flap.getRevealFlap());
        App.window.present();

    }
}