#include "set_node.h"
#define vertex_size 10
#define edge_size 7
void connected_comp();
void verify();
char vertex [vertex_size];
char edge[edge_size][2];
struct set_node* nodes[vertex_size];
int res [10][10];//set when two element are in the same set.
 
void init(){
    int i=0;
    for(;i<vertex_size;i++){
        vertex[i]='a'+i;
        printf("%c\n",vertex[i]);
    }
    edge[0][0]='b';
    edge[0][1]='d';
    
    edge[1][0]='e';
    edge[1][1]='g';
    
    edge[2][0]='a';
    edge[2][1]='c';
    
    edge[3][0]='h';
    edge[3][1]='i';
    
    edge[4][0]='a';
    edge[4][1]='b';
    
    edge[5][0]='e';
    edge[5][1]='f';
    
    edge[6][0]='b';
    edge[6][1]='c';
    i=0;
    for(;i<edge_size;i++){
    printf("%c --- %c\n",edge[i][0], edge[i][1]);
	}    
    //first connected componet.
    res[0][1]=1;
    res[0][2]=1;
    res[0][3]=1;
    res[1][2]=1;
    res[1][3]=1;
    res[2][3]=1;  
       
    //second connected componet.
    res[4][5]=1;
    res[4][6]=1;
    res[5][6]=1;
    
    //third connected componet.
    res[7][8]=1;    
}

int main(int argd, char*  args[]){
    init();
    connected_comp();
    verify();
    return 0;
}

void verify(){
    int i=0;
    int j=0;
    for(;i<vertex_size;i++){
        j=i+1;
         for(;j<vertex_size;j++){
            if((find_set(nodes[i])==find_set(nodes[j]))&&res[i][j]!=1){
printf("i=%d, j=%d, res[i][j]=%d",i,j,res[i][j]);
                printf("problem with node %c and %c\n", vertex[i],vertex[j]);
            }
         }
     }
}

void connected_comp(){
    int i=0;
    for(;i<vertex_size;i++){
        nodes[i] = make_set(&vertex[i]);
    }
    i=0;
/**jj    for(;i<edge_size;i++){
        char a=edge[i][0];
        int ia = a-'a';
        char b=edge[i][1];
        int ib=b -'a';
        struct set_node* nodea=nodes[ia];
        struct set_node* nodeb=nodes[ib];
        
        if(find_set(nodea)!=find_set(nodeb)){
            unite_set(nodea,nodeb);
        }
    }*/
return ;
}

