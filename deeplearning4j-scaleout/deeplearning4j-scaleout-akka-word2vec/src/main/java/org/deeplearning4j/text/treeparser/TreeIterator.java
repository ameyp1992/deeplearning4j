package org.deeplearning4j.text.treeparser;

import org.deeplearning4j.rntn.Tree;
import org.deeplearning4j.word2vec.sentenceiterator.labelaware.LabelAwareSentenceIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tree iterator: iterate over sentences
 * returning trees with labels and everything already
 * preset
 *
 * @author Adam Gibson
 */
public class TreeIterator implements Iterator<List<Tree>> {

    private LabelAwareSentenceIterator sentenceIterator;
    private List<String> labels;
    private TreeVectorizer treeVectorizer;
    private int batchSize = 3;



    public TreeIterator(LabelAwareSentenceIterator sentenceIterator, List<String> labels, TreeVectorizer treeVectorizer,int batchSize) {
        this.sentenceIterator = sentenceIterator;
        this.labels = labels;
        this.treeVectorizer = treeVectorizer;
        this.batchSize = batchSize;
    }

    public TreeIterator(LabelAwareSentenceIterator sentenceIterator, List<String> labels, TreeVectorizer treeVectorizer) {
        this.sentenceIterator = sentenceIterator;
        this.labels = labels;
        this.treeVectorizer = treeVectorizer;
        batchSize = labels.size();
    }

    public TreeIterator(LabelAwareSentenceIterator sentenceIterator, List<String> labels) throws Exception {
        this(sentenceIterator,labels,new TreeVectorizer());
    }


    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return sentenceIterator.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     */
    @Override
    public List<Tree> next() {
        List<Tree> ret = new ArrayList<>();
        try {
            for(int i = 0; i < batchSize; i++)
                if(hasNext())
                    ret.addAll(treeVectorizer.getTreesWithLabels(sentenceIterator.nextSentence(), sentenceIterator.currentLabel(), labels));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    /**
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.  The behavior of an iterator
     * is unspecified if the underlying collection is modified while the
     * iteration is in progress in any way other than by calling this
     * method.
     *
     * @throws UnsupportedOperationException if the {@code remove}
     *                                       operation is not supported by this iterator
     * @throws IllegalStateException         if the {@code next} method has not
     *                                       yet been called, or the {@code remove} method has already
     *                                       been called after the last call to the {@code next}
     *                                       method
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
