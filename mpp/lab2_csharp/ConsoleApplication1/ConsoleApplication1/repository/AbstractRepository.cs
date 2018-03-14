using System.Collections.Generic;

namespace ConsoleApplication1.repository
{
    public class AbstractRepository<T, ID> : IRepository<T, ID> where T : IHasId<ID>
    {
        protected Dictionary<ID, T> lst = new Dictionary<ID, T>();
        
        public AbstractRepository() {
        }
        
        public long Size()
        {
            return lst.Count;
        }

        public virtual T Save(T t)
        {
            lst.Add(t.Id, t);
            return lst.ContainsValue(t) ? default(T) : t;
        }

        public virtual Dictionary<ID, T> GetAll()
        {
            return lst;
        }

        public virtual bool Delete(ID id)
        {
            return lst.Remove(id);
        }

        public T GetOne(ID id)
        {
            return lst[id];
        }

        public virtual T Update(T t)
        {
            if (!lst.ContainsKey(t.Id))
                return t;
            lst[t.Id] = t;
            return default(T);
        }
    }
}