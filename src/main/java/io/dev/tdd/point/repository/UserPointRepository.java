package io.dev.tdd.point.repository;

import io.dev.tdd.point.UserPoint;

public interface UserPointRepository {

     UserPoint selectByUserId(long id);
     UserPoint insertOrUpdate(long id, long amount);


}
