package javax.mail;

public class Quota {
    public String quotaRoot;
    public Resource[] resources;

    public static class Resource {
        public long limit;
        public String name;
        public long usage;

        public Resource(String str, long j, long j2) {
            long j3 = j;
            long j4 = j2;
            this.name = str;
            this.usage = j3;
            this.limit = j4;
        }
    }

    public Quota(String str) {
        this.quotaRoot = str;
    }

    public void setResourceLimit(String str, long j) {
        String str2 = str;
        long j2 = j;
        Resource resource;
        Resource resource2;
        if (this.resources == null) {
            this.resources = new Resource[1];
            Resource[] resourceArr = this.resources;
            resource = r14;
            resource2 = new Resource(str2, 0, j2);
            resourceArr[0] = resource;
            return;
        }
        for (int i = 0; i < this.resources.length; i++) {
            if (this.resources[i].name.equalsIgnoreCase(str2)) {
                this.resources[i].limit = j2;
                return;
            }
        }
        Object obj = new Resource[(this.resources.length + 1)];
        System.arraycopy(this.resources, 0, obj, 0, this.resources.length);
        Object obj2 = obj;
        int length = obj.length - 1;
        resource = r14;
        resource2 = new Resource(str2, 0, j2);
        obj2[length] = resource;
        this.resources = obj;
    }
}
