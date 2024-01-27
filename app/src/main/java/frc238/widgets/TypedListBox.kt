package frc238.widgets

import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.ListBoxRow
import ch.bailu.gtk.gtk.SelectionMode
import javax.annotation.Nullable


/**
 * A ListBox that gets and returns any generic type of ListBoxRow rather than just ListBoxRow
 */
class TypedListBox<T: ListBoxRow>() : ListBox() {

    final var commandsList: ArrayList<T> = ArrayList<T>()
    var lastSelectedRow: ListBoxRow? = null

    init {

        selectionMode = SelectionMode.SINGLE
    }
    public fun append(child: T) {
        super.append(child)
        commandsList += child
    }

    // name += child
    operator fun plusAssign(child: T) {
        super.append(child)
        commandsList += child
    }

    // name[index]
    operator fun get(index: Int): T {
        return commandsList[index]
    }

    // name[index] = child
    operator fun set(index: Int, child: T) {
        commandsList[index] = child
        super.insert(child, index)
    }

    // name -= child
    operator fun minusAssign(child: T) {
        super.remove(child)
        commandsList -= child
    }
    public fun remove(child: T) {
        super.remove(child)
        commandsList -= child
    }

    // child in name
    operator fun contains(child: T): Boolean {
        return commandsList.contains(child)
    }

    // iter in name
    operator fun iterator(): Iterator<T> {
        return commandsList.iterator()
    }

    public fun size(): Int {
        return commandsList.size
    }

    public override fun selectRow(@Nullable row: ListBoxRow?) {
        unselectAll()
        super.selectRow(row)
        lastSelectedRow = row
    }
    public val selectedRowTyped: T
        get() {
            val row = super.getSelectedRow()
            if (row.index != -1) {
                return commandsList[row.index]
            } else {
                return commandsList[lastSelectedRow!!.index]
            }
        }
}
