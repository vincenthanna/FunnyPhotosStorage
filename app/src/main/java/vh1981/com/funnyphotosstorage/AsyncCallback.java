package vh1981.com.funnyphotosstorage;

/**
 * Created by vh1981 on 16. 3. 3.
 */
public interface AsyncCallback<T> {
    public void started();
    public void running(int percentage);
    public void finished(T result);
}
