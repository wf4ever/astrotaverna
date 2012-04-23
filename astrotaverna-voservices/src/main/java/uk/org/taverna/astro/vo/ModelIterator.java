package uk.org.taverna.astro.vo;

import java.util.Iterator;

import javax.help.UnsupportedOperationException;
import javax.swing.ComboBoxModel;
import javax.swing.MutableComboBoxModel;

public class ModelIterator<T> implements Iterator<T>, Iterable<T> {

	private final ComboBoxModel model;
	private int position;
//	private final Class<T> desiredType;

	public ModelIterator(ComboBoxModel model) {
		this.model = model;
		//this.desiredType = desiredType;
		this.position = 0;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return position > model.getSize();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		return (T)model.getElementAt(position++);
	}

	@Override
	public void remove() {
		if (!(model instanceof MutableComboBoxModel)) {
			throw new UnsupportedOperationException(
					"Model is not a MutableComboBoxModel");
		}
		MutableComboBoxModel mutable = (MutableComboBoxModel) model;
		if (position == 0) {
			throw new IllegalStateException("next() not called");
		}
		mutable.removeElement(--position);
	}

}
