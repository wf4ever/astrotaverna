package uk.org.taverna.astro.vorepo;

import java.util.Iterator;

/**
 * An {@link Iterable} {@link Iterator}.
 * 
 * @author stain
 * 
 * @param <T>
 */
public class IterableIterator<T> implements Iterable<T>, Iterator<T> {

	private final Iterator<T> iterator;

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public T next() {
		return iterator.next();
	}

	public void remove() {
		iterator.remove();
	}

	/**
	 * Constructor for subclasses overriding hasNext/next/remove. This sets the
	 * delegate iterator to <code>null</code>.
	 */
	protected IterableIterator() {
		this.iterator = null;
	}

	/**
	 * Wrap an {@link Iterator} so that it becomes {@link Iterable} (and thus
	 * usable in a for-loop)
	 * 
	 * @param iterator
	 */
	public IterableIterator(Iterator<T> iterator) {
		this.iterator = iterator;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

}
