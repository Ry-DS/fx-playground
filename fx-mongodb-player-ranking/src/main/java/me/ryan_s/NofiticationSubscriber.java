package me.ryan_s;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by SimplyBallistic on 3/03/2019
 *
 * @author SimplyBallistic
 **/
public class NofiticationSubscriber implements Subscriber {
    @Override
    public void onSubscribe(Subscription s) {
        s.request(1);
    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
