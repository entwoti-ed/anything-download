<!-- author: CY -->

<template>
    <router-view/>
</template>

<style>
    /* 禁止用户在界面上进行选择 */
    * {
        user-select: none;
    }

    ::-webkit-scrollbar {
        width: 0.375rem;
        height: 0.25rem;
        background-color: rgba(0, 0, 0, 0);
    }

    ::-webkit-scrollbar-thumb {
        background-color: #007bff !important;
    }

    ::-webkit-scrollbar-track {
        background-color: rgba(0, 0, 0, .125);
    }
</style>