package me.ryan_s;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by SimplyBallistic on 3/03/2019
 *
 * @author SimplyBallistic
 **/
public class PrintSubscriber<T> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);

    }

    @Override
    public void onNext(T o) {
        System.out.println(o);
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();

    }

    @Override
    public void onComplete() {
        System.out.println("Finished operation");
    }
}
