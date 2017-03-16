
/**
 * @filename HashFrequencyTable
 * @author Colin St. Claire
 * @created 11/3/15
 * @modified 11/4/15
 *
 */
public interface FrequencyTable<K> {
	void click(K key);
	int count(K key);
}
