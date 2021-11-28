package opgave.samplers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A generator of uniformly distributed samples.
 *
 * Usage:
 * 1. Construct a sampler
 * 2. (Optional) Add elements to your collection using getElements()
 * 3. Collect samples
 * 4. Benchmark using the samples
 */
public class Sampler {
    protected final Random rng;
    protected int numberOfElements;
    protected List<Integer> elements;

    public Sampler(Random rng, int numberOfElements) {
        this.rng = rng;
        if (numberOfElements <= 0) {
            throw new IllegalArgumentException("number of elements is not strictly positive: " + numberOfElements);
        }
        this.numberOfElements = numberOfElements;
        this.elements = new ArrayList<>(numberOfElements);
        for (int i = 0; i < numberOfElements; i++) {
            this.elements.add(i);
        }
        Collections.shuffle(this.elements, rng);
    }

    protected int sample() {
        return rng.nextInt(this.numberOfElements);
    }

    public List<Integer> sample(int numSamples) {
        List<Integer> shuffled = new ArrayList<>(this.elements);
        Collections.shuffle(shuffled, rng);
        List<Integer> samples = new ArrayList<>(numSamples);
        for (int i = 0; i < numSamples; i++) {
            samples.add(shuffled.get(this.sample()));
        }
        return samples;
    }

    public List<Integer> getElements() {
        return elements;
    }
}
