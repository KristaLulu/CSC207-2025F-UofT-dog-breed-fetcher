package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String breed = "hound";
        BreedFetcher breedFetcher = new CachingBreedFetcher(new DogApiBreedFetcher());
        int result = 0;
        try {
            result = getNumberOfSubBreeds(breed, breedFetcher);
        } catch (BreedFetcher.BreedNotFoundException e) {
            System.out.println("Breed not found");
        }
        System.out.println(breed + " has " + result + " sub breeds");

        breed = "cat";
        try {
            result = getNumberOfSubBreeds(breed, breedFetcher);
        } catch (BreedFetcher.BreedNotFoundException e) {
            System.out.println("Breed not found");
        }
        System.out.println(breed + " has " + result + " sub breeds");
    }

    /**
     * Return the number of sub breeds that the given dog breed has according to the
     * provided fetcher.
     * @param breed the name of the dog breed
     * @param breedFetcher the breedFetcher to use
     * @return the number of sub breeds. Zero should be returned if there are no sub breeds
     * returned by the fetcher
     */
    public static int getNumberOfSubBreeds(String breed, BreedFetcher breedFetcher) throws BreedFetcher.BreedNotFoundException {
        // TODO Task 3 implement this code so that it is entirely consistent with its provided documentation.
        // return statement included so that the starter code can compile and run.
        return breedFetcher.getSubBreeds(breed).size();
    }
}