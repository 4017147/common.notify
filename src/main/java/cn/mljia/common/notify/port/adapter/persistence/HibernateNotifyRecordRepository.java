//   Copyright 2012,2013 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package cn.mljia.common.notify.port.adapter.persistence;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import cn.mljia.common.notify.domain.NotifyRecord;
import cn.mljia.common.notify.domain.NotifyRecordRepository;
import cn.mljia.common.notify.exception.NegativeException;
import cn.mljia.ddd.common.port.adapter.persistence.hibernate.HibernateSupperRepository;
 

@Repository
public class HibernateNotifyRecordRepository extends HibernateSupperRepository implements NotifyRecordRepository
{
    
    public HibernateNotifyRecordRepository()
    {
        super();
    }
    
    @Override
    public void add(NotifyRecord notifyRecord)
        throws NegativeException
    {
        // TODO Auto-generated method stub
        try
        {
            this.session().merge(notifyRecord);
        }
        catch (ConstraintViolationException e)
        {
            throw new IllegalStateException("User is not unique.", e);
        }
    }
    
    @Override
    public void remove(NotifyRecord notifyRecord)
        throws NegativeException
    {
        // TODO Auto-generated method stub
        this.session().delete(notifyRecord);
    }
    
    @Override
    public NotifyRecord notifyRecordOfId(String id)
        throws NegativeException
    {
        // TODO Auto-generated method stub
        NotifyRecord notifyRecord =
            (NotifyRecord)this.session()
                .createQuery(" from NotifyRecord notifyRecord where notifyRecord.notifyId = :notifyId ")
                .setParameter("notifyId", id)
                .uniqueResult();
        return notifyRecord;
    }
    
    @Override
    public List<NotifyRecord> notifyRecordOfByPage(Integer pageSize, Integer pageNumber, Integer maxNotifyTimes)
        throws NegativeException
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from tb_common_notify_record  where 1 = 1 and status != 'SUCCESS' and status != 'FAILED' and notify_times < :maxNotifyTimes  ");
        Query query =
            this.session()
                .createSQLQuery(sql.toString()).addEntity(NotifyRecord.class)
                .setParameter("maxNotifyTimes", maxNotifyTimes)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize);
        return query.list();
    }
    
    @Override
    public Integer notifyRecordOfByPageCount(Integer maxNotifyTimes)
        throws NegativeException
    {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from tb_common_notify_record  where 1 = 1 and status != 'SUCCESS' and status != 'FAILED' and notify_times < :maxNotifyTimes  ");
        Query query = this.session().createSQLQuery(sql.toString()).setParameter("maxNotifyTimes", maxNotifyTimes);
        BigInteger count = (BigInteger)query.uniqueResult();
        return count != null ? count.intValue() : 0;
    }
}
