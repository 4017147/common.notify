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

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import cn.mljia.common.notify.domain.NotifyRecordLog;
import cn.mljia.common.notify.domain.NotifyRecordLogRepository;
import cn.mljia.common.notify.exception.NegativeException;
import cn.mljia.ddd.common.port.adapter.persistence.hibernate.HibernateSupperRepository;

@Repository
public class HibernateNotifyRecordLogRepository extends HibernateSupperRepository implements NotifyRecordLogRepository
{
    
    public HibernateNotifyRecordLogRepository()
    {
        super();
    }
    
    @Override
    public void add(NotifyRecordLog notifyRecordLog)
        throws NegativeException
    {
        // TODO Auto-generated method stub
        try
        {
            this.session().merge(notifyRecordLog);
        }
        catch (ConstraintViolationException e)
        {
            throw new IllegalStateException("User is not unique.", e);
        }
    }
    
    @Override
    public void remove(NotifyRecordLog notifyRecordLog)
        throws NegativeException
    {
        // TODO Auto-generated method stub
        this.session().delete(notifyRecordLog);
    }
    
}
