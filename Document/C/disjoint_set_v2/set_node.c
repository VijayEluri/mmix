#include "set_node.h"
/**
 * return the pointer to the first element in the set.
 */
struct set_node* make_set(char* name){
    struct set_node* node = (struct set_node*)malloc(sizeof(struct set_node));
    node->name=name;
    node->parent=node;
    node->rank=0;
    return node;
}
/**
 * unite two sets and 
 * return the pointer to the first element in the set.
 */
struct set_node* unite_set(struct set_node* a, struct set_node*  b){
    struct set_node* na = find_set(a);
    struct set_node* nb = find_set(b);
    if(na->rank > nb->rank){
        nb->parent = na;
        return na;
    }else{
        na->parent=nb;
        if(na->rank == nb->rank){
            nb->rank++;
        }
        return nb;
    }
    
    
}

/**
 * find the representative for the node 
 * return the pointer to the first element in the set.
 */
struct set_node* find_set(struct set_node* a){
    
    if(a->parent!=a){
        a->parent = find_set(a->parent);        
    }
    return a-> parent;    
}

