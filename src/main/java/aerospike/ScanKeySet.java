package aerospike;

import com.aerospike.client.ScanCallback;
import java.util.ArrayList;
import java.util.List;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ScanPolicy;

public class ScanKeySet implements ScanCallback {

    ArrayList<Key> keyList;

    public ScanKeySet() {
        this.keyList = new ArrayList<Key>();
    }

    public List<Key> runScan(AerospikeClient client, String namespace, String set) {
        ScanPolicy policy = new ScanPolicy();
        client.scanAll(policy, namespace, set, this);
        return keyList;
    }

    public void scanCallback(Key key, Record record) {
        keyList.add(key);
    }

}