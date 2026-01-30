package com.insett.indicesservice.domain.dao;

import com.insett.indicesservice.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    /**
 * Finds a product matching the given title and brand.
 *
 * @param title the product's title to match
 * @param brand the product's brand to match
 * @return an {@link Optional} containing the matching Product, or empty if no match is found
 */
Optional<Product> findByTitleAndBrand(String title, String brand);

    /**
 * Finds products that match the specified title and brand.
 *
 * @param title  the product title to match
 * @param brand  the product brand to match
 * @return       a list of Product entities matching the given title and brand; empty if none found
 */
List<Product> findAllByTitleAndCategory(String title, String brand);

    /**
 * Finds products that match the given title.
 *
 * @param name the exact or analyzed product title to search for
 * @return SearchHits containing matching Product documents
 */
SearchHits<Product> findByTitle(String name);

    /**
 * Finds products that match both the specified category and brand.
 *
 * @param category the category to match
 * @param brand the brand to match
 * @return SearchHits containing products that match the given category and brand
 */
SearchHits<Product> findByCategoryAndBrand(String category, String brand);

    /**
 * Finds products matching the specified category.
 *
 * @param category the category value to match
 * @return a SearchHits of Product objects whose category equals the given category
 */
SearchHits<Product> findByCategory(String category);

    /**
 * Finds products matching the given category and returns them as a paginated search page.
 *
 * @param category the product category to match
 * @param pageable pagination and sorting information for the search
 * @return a SearchPage of Product containing search hits for products that match the category according to the provided Pageable
 */
SearchPage<Product> findByCategory(String category, Pageable pageable);

    /**
 * Finds products whose category is one of the given category names.
 *
 * @param categories the list of category names to match
 * @return SearchHits containing products whose category is in the provided list
 */
SearchHits<Product> findByCategoryIn(List<String> categories);

    /**
 * Finds products whose price is less than the given threshold.
 *
 * @param price the upper price threshold; products with price strictly less than this value are matched
 * @return SearchHits containing products with price strictly less than {@code price}
 */
SearchHits<Product> findByPriceLessThan(Integer price);

    /**
 * Finds products with price between two specified values and orders them by the provided sort.
 *
 * @param from the lower bound of the price range (inclusive)
 * @param to the upper bound of the price range (inclusive)
 * @param sort the sort criteria to apply to the search results
 * @return the search hits containing products whose price is between `from` and `to`, ordered per `sort`
 */
SearchHits<Product> findByPriceBetween(Integer from, Integer to, Sort sort);
}