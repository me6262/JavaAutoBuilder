

import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.ListBoxRow
import frc238.widgets.AutoCommandRow


/**
 * A ListBox that gets and returns any generic type of ListBoxRow rather than just ListBoxRow
 */
class TypedListBox<T: ListBoxRow>() : ListBox() {

    final var commandsList: ArrayList<T> = ArrayList<T>()



    operator fun plusAssign(child: T) {
        super.append(child)
        commandsList += child
    }

    operator fun get(index: Int): T {
        return commandsList[index]
    }

    operator fun set(index: Int, child: T) {
        commandsList[index] = child
        super.remove(super.getRowAtIndex(index))
        super.insert(child, index)
    }

    operator fun minusAssign(child: T) {
        super.remove(child)
        commandsList -= child
    }

    operator fun contains(child: T): Boolean {
        return commandsList.contains(child)
    }

    operator fun iterator(): Iterator<T> {
        return commandsList.iterator()
    }

}
