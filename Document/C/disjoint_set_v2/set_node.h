struct set_node{
    char* name;
    struct set_node* parent;  
    int rank;//rank is 0 for single node set.
};

struct set_node* make_set(char* name);
struct set_node* unite_set(struct set_node* a, struct set_node*  b);
struct set_node* find_set(struct set_node* a);
