import EmployeeLoginPage from '../pages/EmployeeLoginPage.vue'
import EmployeeHomePage from '../pages/EmployeeHomePage.vue'
import BasicLayout from "../layouts/BasicLayout.vue";
import {createRouter, createWebHistory} from "vue-router";

const routes = [
    {
        path: '/employee',
        component: BasicLayout,
        children: [
            {
                path: 'home',
                component: EmployeeHomePage,
            },
        ],
    },
    {
        path: '/employee/login',
        component: EmployeeLoginPage,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
